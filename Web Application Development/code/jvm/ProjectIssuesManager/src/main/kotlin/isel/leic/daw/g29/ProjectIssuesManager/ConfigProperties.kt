package isel.leic.daw.g29.ProjectIssuesManager

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("app")
data class ConfigProperties (
    val dbConnString: String
)