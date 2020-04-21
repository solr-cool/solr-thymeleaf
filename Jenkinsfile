pipeline {
    agent {
        docker {
            image 'hub.s24.com/s24/openjdk-build-agent:11.0.6-5'
            args """
                  --group-add 999
                  --network=host
                  -v /etc/passwd:/etc/passwd
                  -v /etc/group:/etc/group
                  -v ${JENKINS_HOME}/.m2:/www/jenkins-ci/.m2
                  -v ${JENKINS_HOME}/.ssh:/www/jenkins-ci/.ssh
                  """
        }
    }

    options {
        ansiColor 'xterm'
        buildDiscarder(logRotator(numToKeepStr: '30', artifactNumToKeepStr: '1'))
        timestamps()
        timeout(time: 30, unit: 'MINUTES')
        disableConcurrentBuilds()
    }

    stages {
        stage('Build and test') {
            steps {
                sh './mvnw -B -Dstyle.color=always -Djansi.force=true clean verify'
            }
            post {
                always {
                    junit 'target/**/*.xml'
                }
            }
        }

        stage('Deploy to repository') {
            when {
                branch 'master'
            }
            steps {
                sh "./mvnw -B -Dstyle.color=always -Djansi.force=true -Dmaven.resources.skip -Dmaven.main.skip -Dmaven.test.skip -DskipTests -Dmaven.package.skip deploy"
            }
        }
    }
}
