pipeline{
    agent any

    stages{
        stage('Build'){
            steps{
                sh 'docker build -t dealservice .'
            }
        stage('Test'){
            steps {
                sh 'docker run dealservice:latest ./gradlew test'
            }
        stage('Push'){
            steps{
                sh 'docker tag dealservice:latest garethpark/dealservice:latest'
                sh 'docker push garethpark/dealservice:latest'
            }
        }
    }
}
