pipeline {
    agent  'any' 

    stages {
        stage('Source') {
            steps {
                git 'https://github.com/srayuso/unir-cicd.git'
            }
        }
        stage('Build') {
            steps {
                echo 'Building stage!'
                sh 'make build'
            }
        }
        stage('Unit tests') {
            steps {
                sh 'make test-unit'
                archiveArtifacts artifacts: 'results/*.xml'
            }
        }
        stage('API tests') {
            steps {
                echo 'Running API tests...'
                sh 'make test-api'
                archiveArtifacts artifacts: 'results/api/*.xml'
            }
        }
        stage('E2E tests') {
            steps {
                echo 'Running E2E tests...'
                sh 'make test-e2e'
                archiveArtifacts artifacts: 'results/e2e/*.xml'
            }
        }
    }

    post {
        always {
            junit 'results/**/*.xml'
            cleanWs()
        }
        failure {
            // Simulación de correo
            echo "Pipeline failed! Job: ${env.JOB_NAME}, Build: ${env.BUILD_NUMBER}"
            // mail to: 'team@company.com',
            //      subject: "Pipeline failed: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
            //      body: "Check Jenkins for details."
        }
    }
}
