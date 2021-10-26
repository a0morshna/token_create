import com.bettercloud.vault.Vault
import com.bettercloud.vault.VaultConfig
import com.bettercloud.vault.response.LogicalResponse

private static void create_token(VAULT_ADDR, VAULT_TOKEN) {

    final VaultConfig config = new VaultConfig()
            .address(VAULT_ADDR)                            // Defaults to "VAULT_ADDR" environment variable
            .token(VAULT_TOKEN)                             // Defaults to "VAULT_TOKEN" environment variable
            .readTimeout(5)                                 // Defaults to "VAULT_READ_TIMEOUT" environment variable
            .build()

    final Vault vault = new Vault(config)

}