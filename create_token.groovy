import com.bettercloud.vault.Vault
import com.bettercloud.vault.VaultConfig
import com.bettercloud.vault.response.LogicalResponse
import com.bettercloud.vault.api.Auth 
import com.bettercloud.vault.SslConfig
import hudson.tasks.Mailer
import hudson.model.User

def vault_execute(){
    VaultConfig vaultConfig = new VaultConfig()
            .sslConfig(new SslConfig().verify(false).build())
            .address(System.getenv('VAULT_ADDRESS').trim())
            .token(System.getenv('VAULT_TOKEN').trim())
            .build()
    final Vault vault = new Vault(vaultConfig)
    
    
    def response = vault.auth().createToken(
        new Auth.TokenRequest()
            .polices(Arrays.asList("admin_kv"))
            .ttl("5h") 
        );
    
    final String token = response.getAuthClientToken();
    return token   
}

def token = vault_execute()

pipeline {
    agent {
        label 'jenkins-slave-tf'
    }

    stages{
        stage("Token"){
            steps{
                script{
                    // send teams notification
                    // use email person who did this job
                    def email = User.get(currentBuild.rawBuild.getCause(Cause.UserIdCause).getUserId()).getProperty(Mailer.UserProperty.class).getAddress();
                    echo email
                    mail to: email, subject: "Vault token", body: "Token - $token", from: "ak265@tmw.com"
                }
            }
        }
    }
}