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
                        branches: [[name: '*/sarra']],
                        userRemoteConfigs: [[
                            url: 'https://github.com/SarraBenHamouda/Jenkins.git',
                            credentialsId: 'git-credentials'
                        ]]
                    ])
                }
            }
        }

        stage('Setup Maven') {
            steps {
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
                             -Dsonar.projectKey=devops-projet \
                             -Dsonar.host.url=http://localhost:9000 \
                             -Dsonar.login=e156ee25c595f8687b68568d8348b1113ef792c4
                             -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml || true
                        '''
                    }
                }
            }
        }

        stage('Run Tests with Spring Profile') {
            steps {
                sh 'mvn test -Dspring.profiles.active=test'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${DOCKER_IMAGE} ."
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withDockerRegistry([credentialsId: 'docker-hub-credentials', url: 'https://index.docker.io/v1/']) {
                    sh "docker push ${DOCKER_IMAGE}"
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                withCredentials([
                    usernamePassword(credentialsId: 'nexus-credentials', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASS')
                ]) {
                    sh '''
                        mvn deploy \
                          -DaltDeploymentRepository=nexus-releases::default::http://localhost:8081/repository/maven-releases/ \
                          -Dnexus.username=${NEXUS_USER} \
                          -Dnexus.password=${NEXUS_PASS} || true
                    '''
                }
            }
        }
    }

    post {
        failure {
            script {
                echo "Build Failed! Check logs for errors."
                sh 'docker rmi ${DOCKER_IMAGE} || true'
            }
        }
    }
}