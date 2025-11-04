pipeline {
  agent any

  tools {
    jdk   'jdk-21'
    maven 'maven-3.9'
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
        echo 'Running Cucumber API tests...'
        bat 'mvn -B clean test -Dtest=CucumberRunnerTest'
      }
    }

    stage('Publish Reports') {
      steps {
        echo 'Publishing Cucumber API reports...'

        junit testResults: 'Report/*.junit', allowEmptyResults: true

        script {
          def candidates = [
            'Report/cucumber.html',
            'Report/index.html',
            'Report/cucumber/index.html',
            'Report/cucumber/cucumber.html'
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
