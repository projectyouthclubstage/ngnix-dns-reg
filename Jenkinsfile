import java.text.SimpleDateFormat
import groovy.json.JsonSlurper

pipeline{

agent none
  environment {
     def mybuildverison = getBuildVersion(env.BUILD_NUMBER)
     def projektname = env.JOB_NAME.replace("/master","").replace("projectyouthclubstage/","")
     def registry = "192.168.233.1:5000/${projektname}"
  }

  // Pipeline Stages start here
  // Requeres at least one stage

stages{


    // Run Maven build, skipping tests
    stage('Build'){
    agent {
        docker {
            image 'arm32v7/maven'
        }
    }
     steps {
        sh "mvn -B clean install -DskipTests=true"
        }
    }

    // Run Maven unit tests
    stage('Unit Test'){
    agent {
       docker {
           image 'arm32v7/maven'
          }
    }
     steps {
        sh "mvn -B test"
        }
    }




       stage('docker build')
       {
          agent {
               label 'master'
           }
           steps{
            script{
                if (env.BRANCH_NAME == 'master') {
               dockerImage = docker.build registry + ":$mybuildverison"
               dockerImage.push()
               }
              }
           }
       }

            stage('Docker deploy'){
                      agent {
                           label 'master'
                       }
                       steps{



                        script{
                           if (env.BRANCH_NAME == 'master') {
                             dockerDeploy(mybuildverison,registry,projektname,"","","8080")
                           }

                          }
                        }
            }
        }
          post {
            failure {
              script{
                sh "docker stack rm $projektname-$mybuildverison"
              }
            }
          }
     }



def getBuildVersion(String buildnr){
    def dateFormat = new SimpleDateFormat("yyyyMMddHHmm")
    def date = new Date()
    return dateFormat.format(date)+buildnr
}

def dockerDeploy(String mybuildverison,String registry, String projektname, String dns, String dnsblue, String port){


                      sh "mkdir -p target"
                      sh "rm -f target/*.yml"
                      def regescape = registry.replace("/","\\/")
                      sh "cat docker-compose-template.yml | sed -e 's/{image}/$regescape:$mybuildverison/g;s/{alias}/$projektname-$mybuildverison/g' >> target/docker-compose.yml"
                      def version = sh (
                          script: 'docker stack ls |grep '+projektname+'| cut -d \" \" -f1',
                          returnStdout: true
                      ).trim()
                      if(version != "")
                      {
                        sh "docker stack rm "+version
                      }

                      //sh "docker stack rm "+version
                      sh "docker stack deploy --compose-file target/docker-compose.yml "+projektname+"-"+"$mybuildverison"

}

