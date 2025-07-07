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
                            /opt/homebrew/bin/helm push dealservice-*.tgz oci://registry-1.docker.io/${DOCKER_USERNAME}
                        '''
                    }
                }
            }
        }

        stage('Deploy to OpenShift') {
            steps {
                withCredentials([string(credentialsId: 'openshift-token', variable: 'OPENSHIFT_TOKEN')]) {
                    sh '''
                        # Log in to OpenShift
                        oc login --token=$OPENSHIFT_TOKEN --server=https://api.rm2.thpm.p1.openshiftapps.com:6443

                        # Set the project/namespace
                        oc project your-namespace

                        # Deploy or upgrade using Helm
                        helm upgrade --install dealservice oci://registry-1.docker.io/garethpark/dealservice \
                          --version 0.0.1 \
                          --namespace your-namespace \
                          --set image.repository=garethpark/dealservice \
                          --set image.tag=latest
                    '''
                }
            }
        }
    }
}
