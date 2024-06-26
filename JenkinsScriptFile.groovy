pipeline {
    environment {
        repository = "majanada2/allergesafediet_app"
    }

    agent any
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
            post {
                success {
                    echo 'Successfully Cloned Repository'
                }
                failure {
                    error 'This pipeline stops here...'
                }
            }
        }

        stage('Build gradle') {
            steps {
                script {
                    sh './gradlew clean build'
                }
            }
            post {
                success {
                    echo "Successfully Build gradle"
                }
                failure {
                    echo "Fail to build gradle"
                    error 'This pipeline stops here...'
                }
            }
        }

        stage('Debug') {
            steps {
                echo 'Current Working Directory:'
                sh 'pwd'
            }
        }

        stage('Build Docker UI') {
            steps {
                echo 'Build Docker UI'
                dir('UI') {
                    script {
                        dockerImageUI = docker.build("${repository}:UI", "--platform linux/amd64 .")
                    }
                }
            }
            post {
                failure {
                    error 'This pipeline stops here...'
                }
            }
        }

        stage('Debug After Build Docker UI') {
            steps {
                echo 'Current Working Directory:'
                sh 'pwd'
            }
        }

        stage('Build Docker API') {
            steps {
                echo 'API'
                dir('API') {
                    script {
                        withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'AWS_S3_Credentials', accessKeyVariable: 'AWS_ACCESS_KEY_ID', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {
                            def dockerTag = "${repository}:API"
                            dockerImageAPI = docker.build(dockerTag, "--platform linux/amd64 --build-arg AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID --build-arg AWS_SECRET_ACCESS_KEY=$AWS_SECRET_ACCESS_KEY .")
                        }
                    }
                }
            }
            post {
                failure {
                    error 'This pipeline stops here...'
                }
            }
        }


        stage('Push Docker UI') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'dockerhub_token', usernameVariable: 'DOCKERHUB_USER', passwordVariable: 'DOCKERHUB_PASS')]) {
                        sh "docker login -u $DOCKERHUB_USER -p $DOCKERHUB_PASS"
                        sh "docker push ${repository}:UI"
                    }
                }
            }
            post {
                failure {
                    error 'This pipeline stops here...'
                }
            }
        }

        stage('Push Docker API') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'dockerhub_token', usernameVariable: 'DOCKERHUB_USER', passwordVariable: 'DOCKERHUB_PASS')]) {
                        sh "docker login -u $DOCKERHUB_USER -p $DOCKERHUB_PASS"
                        sh "docker push ${repository}:API"
                    }
                }
            }
            post {
                failure {
                    error 'This pipeline stops here...'
                }
            }
        }
    }
}
