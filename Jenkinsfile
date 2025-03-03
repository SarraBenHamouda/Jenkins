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
                sh 'mvn --version'
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

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_LOGIN')]) {
                        sh '''
                           mvn sonar:sonar \
                             -Dsonar.projectKey=devops-projet-key \
                             -Dsonar.host.url=http://192.168.169.32:9000 \
                             -Dsonar.login=2f78cf146a16b840e4cce2319889a6d60c7cd7b5
                        '''
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${DOCKER_IMAGE} ."
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withDockerRegistry([credentialsId: 'docker-hub-credentials', url: '']) {
                    sh "docker push ${DOCKER_IMAGE}"
                }
            }
        }

        stage('Run Tests with Spring Profile') {
            steps {
                sh 'mvn test -Dspring.profiles.active=test'
            }
        }
    }
}
