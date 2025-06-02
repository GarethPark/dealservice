pipeline {
    agent any

    environment {
        CHART_NAME = 'dealservice'
        NEXT_VERSION = '0.0.1'
        HELM_PATH = '/opt/homebrew/bin/helm'
    }

    stages {
        stage('Build') {
            steps {
                sh './mvnw clean install'
            }
        }
        
        stage('Build Container') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                    sh './mvnw jib:build -Djib.to.auth.username=${DOCKER_USERNAME} -Djib.to.auth.password=${DOCKER_PASSWORD}'
                }
            }
        }

        stage('Check Helm') {
            steps {
                sh 'helm version'
            }
        }

        stage('Package Helm Chart') {
            steps {
                dir('./helm/dealservice') {
                    sh '''
                        source ~/.zshrc
                        /opt/homebrew/bin/helm package .
                    '''
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh '''
                            source ~/.zshrc
                            /opt/homebrew/bin/helm push dealservice-${NEXT_VERSION}.tgz oci://registry-1.docker.io/${DOCKER_USERNAME}
                        '''
                    }
                }
            }
        }
    }
}
