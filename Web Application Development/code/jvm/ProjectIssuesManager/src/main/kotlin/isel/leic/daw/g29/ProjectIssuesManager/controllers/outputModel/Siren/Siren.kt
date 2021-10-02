package isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import java.net.URI

/**
 * For details regarding the Siren media type, see <a href="https://github.com/kevinswiber/siren">Siren</a>
 */

/**
 * Gets a Siren self link for the given URI
 *
 * @param uri   the string with the self URI
 * @return the resulting Ssiren link
 * added URI version
 */
fun selfLink(uri: URI) = SirenLink(rel = listOf("self"), href = uri)
fun selfLink(uri: String) = SirenLink(rel = listOf("self"), href = URI(uri))
/**
 * Class whose instances represent links as they are represented in Siren.
 */
data class SirenLink(
        val rel: List<String>,
        val href: URI,
        val title: String? = null,
        val type: MediaType? = null)

/**
 * Class whose instances represent actions that are included in a siren entity.
 */
data class SirenAction(
        val name: String,
        val title: String? = null,
        val method: HttpMethod? = null,
        val href: URI,
        @JsonProperty("class")
        val clazz: List<String>? = null,
        @JsonSerialize(using = ToStringSerializer::class)
        val type: MediaType? = null,
        val fields: List<Field>? = null
) {
    /**
     * Represents action's fields
     */
    data class Field(
            val name: String,
            val type: String? = null,
            val value: String? = null,
            val title: String? = null
    )
}

data class SirenEntity<T>(
        @JsonProperty("class") val clazz: List<String>? = null,
        val properties: T? =null,
        val entities: List<SubEntity>? = null,
        val actions: List<SirenAction>? = null,
        val links: List<SirenLink>? = null,
        val title: String? = null)


/**
 * Base class for admissible sub entities, namely, [EmbeddedLink] and [EmbeddedEntity].
 * Notice that this is a closed class hierarchy.
 */
sealed class SubEntity

data class EmbeddedLink(
        @JsonProperty("class")
        val clazz: List<String>? = null,
        val rel: List<String>,
        val href: URI,
        @JsonSerialize(using = ToStringSerializer::class)
        val type: MediaType? = null,
        val title: String? = null
) : SubEntity()

data class EmbeddedEntity<T>(
        @JsonProperty("class") val clazz: List<String>? = null,
        val rel: List<String>,
        val properties: T? =null,
        val entities: List<SubEntity>? = null,
        val links: List<SirenLink>? = null,
        val actions: SirenAction? = null,
        val title: String? = null
) : SubEntity()