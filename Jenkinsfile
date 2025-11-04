pipeline {
  agent any

  tools {
    jdk   'jdk-21'
    maven 'maven-3.9'   // must match name in Global Tool Configuration
  }

  environment {
    // json-server base URL (you start it manually)
    BASE_URL = 'http://localhost:3000'
  }

  stages {

    stage('Checkout') {
      steps {
        echo 'Checking out source code from GitHub...'
        checkout scm
      }
    }

    stage('Build & Test API') {
      steps {
        echo "Running API tests against ${env.BASE_URL}"

        // If your tests read System.getProperty("baseUrl"), this passes it in:
        bat 'mvn -B -DbaseUrl=%BASE_URL% clean test'
      }
    }

    stage('Publish Reports') {
      steps {
        echo 'Publishing Cucumber API reports...'

        // 1) JUnit XML (from "junit:Report/cucumber.junit" if you want trend graphs)
        // allowEmptyResults so pipeline doesn't fail if there's no XML
        junit testResults: 'Report/*.junit', allowEmptyResults: true

        // 2) Publish the Cucumber HTML report your runner generates: Report/cucumber.html
        script {
          def candidates = [
            'Report/cucumber.html',
            'Report/index.html',
            'Report/cucumber/index.html',
            'Report/cucumber/cucumber.html',
          ]

          def found = candidates.find { fileExists(it) }
          if (found) {
            echo "Publishing Cucumber report: ${found}"
            def dir  = found.substring(0, found.lastIndexOf('/'))
            def file = found.substring(found.lastIndexOf('/') + 1)
            publishHTML([
              reportDir: dir,
              reportFiles: file,
              reportName: 'Cucumber API Report',
              keepAll: true,
              alwaysLinkToLastBuild: true,
              allowMissing: false
            ])
          } else {
            echo "No Cucumber HTML report found in: ${candidates}"
          }
        }

        // 3) Archive both Report and target folders as artifacts
        archiveArtifacts artifacts: 'Report/**, target/**', fingerprint: true
      }
    }
  }

  post {
    always {
      echo 'Pipeline finished.'
    }
  }
}
