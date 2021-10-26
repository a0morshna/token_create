import com.bettercloud.vault.Vault
import com.bettercloud.vault.VaultConfig
import com.bettercloud.vault.response.LogicalResponse


private static void create_token() {
    final Vault vault = container.getRootVault();

    final AuthResponse = vault.auth().createToken(
        new Auth.TokenRequest()
            .id(UUID.randomUUID())
            .polices(Arrays.asList("policy"))
            .noParent(true)
            .noDefaultPolicy(false)
            .ttl("1h")
            .displayName("display name")
            .numUses(1L)
            .renewable(true)
            .type("service")
            .explicitMaxTtl("5h")
            .period("2h")
            .entityAlias("entityId")
        );

    final String token = response.getAuthClientToken();
    final String accessor = response.getTokenAccessor();

    assertNotNull(accessor);
    assertNotNull(token);
    assertEquals(2, response.getAuthPolicies().size());
    assertEquals("default", response.getAuthPolicies().get(0));
    assertEquals("policy", response.getAuthPolicies().get(1));
    assertEquals(7200, response.getAuthLeaseDuration());

    notify(token)
}


private static void setToken(token){
    VaultConfig vaultConfig = new VaultConfig()
            .sslConfig(new SslConfig().verify(false).build())
            .address(System.getenv('VAULT_ADDRESS').trim())
            .token(token)
            .build()
    final Vault vault = new Vault(vaultConfig)
}


def notify(token) {
    // send teams notification
    office365ConnectorSend message: "Token: ${token}", webhookUrl: EMAIL_TEAMS
}


/* email notification
pipeline {
    agent any
    post {
        success {
            mail to: 'name@example.com',
                subject: "Vault token",
                body: "Token - $token"
        }
    }
} */
