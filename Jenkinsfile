pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "sarra7/my-nginx:latest"
    }

    stages {
        stage('Clone Repository') {
            steps {
                script {
                    checkout([
                        $class: 'GitSCM',
                        branches: [[name: 'sarra-dev']], 
                        userRemoteConfigs: [[
                            url: 'https://github.com/kenza-20/Devops-projet.git',
                            credentialsId: 'git-credentials'  
                        ]]
                    ])
                }
            }
        }

        stage('Setup Maven') {
            steps {
                sh 'echo "Setting up Maven..."'
            }
        }

        stage('Maven Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Run Unit Tests') {
            steps {
                sh 'mvn test' 
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t ${DOCKER_IMAGE} .'  
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withDockerRegistry([credentialsId: 'docker-hub-credentials', url: '']) {
                    sh 'docker push ${DOCKER_IMAGE}'
                }
            }
        }
    }
}
