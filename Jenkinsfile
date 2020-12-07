pipeline {
    agent any

    tools {
        jdk 'openjdk-11-2'
        //maven 'apache-maven-3.x'
    }

    //environment {
        //JAVA_HOME =
    //}

    stages {
        stage ('Compile Stage') {
            steps {
                withMaven(maven: 'maven_3_6_3') {
                    sh 'mvn clean compile'
                }
            }
        }

        stage ('Testing Stage') {
            steps {
                withMaven(maven: 'maven_3_6_3', jdk: 'openjdk-11-2') {
                    sh 'mvn test'
                }
            }
        }

        stage ('Deploy Artifact Stage') {
            steps {
                withMaven(maven: 'maven_3_6_3', jdk: 'openjdk-11-2') {
                    sh 'mvn install -Dmaven.test.skip=true'
                }
            }
        }

        stage ('Build & Deploy Image Stage') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', 'glazaror-dockerhub') {
                        def customImage = docker.build('glazaror/cliente-microservice')
                        customImage.push()
                    }
                }
            }
        }

        stage ('Start Application') {
            steps {
                script {
                    def remote = [:]
                    remote.name = "microservices-glazaror-test-server"
                    remote.host = "microservices-glazaror-test-server.southcentralus.cloudapp.azure.com"
                    remote.allowAnyHosts = true

                    withCredentials([usernamePassword(credentialsId: 'glazaror-azure-ssh', passwordVariable: 'password', usernameVariable: 'userName')]) {
                        remote.user = userName
                        remote.password = password

                        try {
                            sshCommand remote: remote, command: 'docker stop cliente-microservice && docker rm cliente-microservice && docker run -p 8090:8090 -d --name cliente-microservice glazaror/cliente-microservice'
                            echo 'cliente-microservice was running and it was stopped'
                        } catch (Exception e) {
                            echo 'starting cliente-microservice'
                            sshCommand remote: remote, command: 'docker run -p 8090:8090 -d --name cliente-microservice glazaror/cliente-microservice'
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
