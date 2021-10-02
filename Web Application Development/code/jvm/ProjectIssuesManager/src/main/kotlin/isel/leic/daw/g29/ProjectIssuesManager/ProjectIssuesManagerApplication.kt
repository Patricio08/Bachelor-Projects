package isel.leic.daw.g29.ProjectIssuesManager

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

import com.fasterxml.jackson.annotation.JsonInclude
import isel.leic.daw.g29.ProjectIssuesManager.authentication.UserInfo
import isel.leic.daw.g29.ProjectIssuesManager.authentication.verifyBasicSchemeCredentials
import isel.leic.daw.g29.ProjectIssuesManager.services.CollaboratorServices
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.postgresql.ds.PGSimpleDataSource
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.sql.DataSource

@SpringBootApplication
@ConfigurationPropertiesScan
class ProjectIssuesManagerApplication(
	private val configProperties: ConfigProperties
) {
	@Bean
	fun jackson2ObjectMapperBuilder() = Jackson2ObjectMapperBuilder()
		.serializationInclusion(JsonInclude.Include.NON_NULL)

	@Bean
	fun dataSource() = PGSimpleDataSource().apply {
		setURL(configProperties.dbConnString)
	}

	@Bean
	fun jdbi(dataSource: DataSource) = Jdbi.create(dataSource).apply {
		installPlugin(KotlinPlugin())
	}

	@Bean
	fun authenticationProvider(): Function2<String, CollaboratorServices, UserInfo?> = ::verifyBasicSchemeCredentials

}

@Configuration
@EnableWebMvc
class ApiConfig : WebMvcConfigurer {
	override fun addInterceptors(registry: InterceptorRegistry) {
		registry.addInterceptor(Interceptor())
	}
}

private val log = LoggerFactory.getLogger(ProjectIssuesManagerApplication::class.java)


fun main(args: Array<String>) {
	val context = runApplication<ProjectIssuesManagerApplication>(*args)
	context.getBeansWithAnnotation(RestController::class.java).forEach { (key, value) ->
		log.info("Found controller bean '{}' with type '{}'", key, value.javaClass.name)
	}
}
