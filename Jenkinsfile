pipeline {
    agent any

    tools {
        // match tool names from Global Tool Configuration
        jdk 'jdk-21'
        maven 'maven-3.9'
    }

    environment {
        BASE_URL = 'http://localhost:3000'
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Checking out source code from GitHub...'
                checkout scm
            }
        }

        stage('Start JSON Server') {
            steps {
                echo 'Starting json-server on port 3000...'

                // If 'json-server' is not recognized, replace with full path to json-server.cmd
                bat '''
                cd "%WORKSPACE%"
                start "" json-server --watch db.json --port 3000
                '''
                // wait ~5 seconds so json-server is ready
                bat 'ping 127.0.0.1 -n 6 >nul'
            }
        }

        stage('Build Project') {
            steps {
                echo 'Compiling project and downloading dependencies...'
                bat 'mvn clean compile -DskipTests'
            }
        }

        stage('Run API Tests') {
            steps {
                echo 'Running Cucumber + Rest Assured tests...'
                // If your code reads System.getProperty("baseUrl"), you can do:
                // bat 'mvn test -DbaseUrl=%BASE_URL%'
                bat 'mvn test'
            }
        }

        stage('Publish Reports') {
            steps {
                echo 'Publishing JUnit and Cucumber reports...'

                // 1) JUnit reports (from Surefire/TestNG)
                junit 'target/surefire-reports/*.xml'

                // 2) Cucumber reports (needs Cucumber Reports plugin)
                cucumber buildStatus: 'UNSTABLE',
                          fileIncludePattern: 'cucumber-report.json',
                          jsonReportDirectory: 'target',
                          sortingMethod: 'ALPHABETICAL',
                          trendsLimit: 10
            }
        }
    }

    post {
        always {
            echo 'Pipeline finished. Cleaning up...'

            // Kill json-server / node so port 3000 is free for next build
            bat '''
            taskkill /F /IM node.exe || echo "No node.exe processes to kill"
            '''
        }
    }
}
