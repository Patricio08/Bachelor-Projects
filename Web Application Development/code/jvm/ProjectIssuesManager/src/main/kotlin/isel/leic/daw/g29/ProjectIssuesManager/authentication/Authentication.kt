package isel.leic.daw.g29.ProjectIssuesManager.authentication

import isel.leic.daw.g29.ProjectIssuesManager.DataStore
import isel.leic.daw.g29.ProjectIssuesManager.controllers.COLLABORATORS_PART
import isel.leic.daw.g29.ProjectIssuesManager.controllers.HOST_PATH
import isel.leic.daw.g29.ProjectIssuesManager.services.CollaboratorServices
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.util.Base64Utils
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Contract to be supported by credential's verification procedures
 */
typealias CredentialsVerifier = (credentials: String, collaboratorServices: CollaboratorServices) -> UserInfo?

/**
 * Closed hierarchy (i.e. a sum type) used to support our simplistic RBAC (role-based access control) approach
 */
sealed class UserInfo(val name: String)
class Guest(name: String) : UserInfo(name)
class Owner(name: String) : UserInfo(name)

const val USER_ATTRIBUTE_KEY = "user-attribute"
const val BASIC_SCHEME = "Basic"
const val CHALLENGE_HEADER = "WWW-Authenticate"
private val logger = LoggerFactory.getLogger(AuthenticationFilter::class.java)
/**
 * Implementation of our simplistic authentication scheme
 *
 * @param challengeResponse the content of the Authorization Header (the challenge response)
 * @return  the [UserInfo] instance representing the user role, or null if the credentials are invalid
 */
fun verifyBasicSchemeCredentials(challengeResponse: String, collaboratorServices: CollaboratorServices): UserInfo? {

    fun verifyUserCredentials(userId: String, pwd: String): UserInfo? {
        val collaborator = collaboratorServices.getCollaborator(userId)
        logger.info("----------> ", collaborator.name)
        if (collaborator.password == pwd)
            return Owner(userId)
        else {
            return null
        }
    }

    val trimmedChallengeResponse = challengeResponse.trim()
    return if (trimmedChallengeResponse.startsWith(BASIC_SCHEME, ignoreCase = true)) {
        val userCredentials = trimmedChallengeResponse.drop(BASIC_SCHEME.length + 1).trim()
        val (userId, pwd) = String(Base64Utils.decodeFromString(userCredentials)).split(':')
        verifyUserCredentials(userId, pwd)
    }
    else {
        null
    }
}

/**
 * This is a sample filter that performs request logging.
 */
@Component
class AuthenticationFilter(private val credentialsVerifier: CredentialsVerifier, private val collaboratorServices: CollaboratorServices, private val dataStore: DataStore) : Filter {

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {

        val httpRequest = request as HttpServletRequest

        logger.info("INCOMING HTTP REQUEST.")

        val time = System.currentTimeMillis()
        chain?.doFilter(request, response)
        val name = httpRequest.getAttribute("Handle").toString()

        dataStore.inc(name)
        dataStore.add(System.currentTimeMillis() - time)

        println(dataStore.getCounter(name))
        println(dataStore.getAverage())


    /*if(httpRequest.requestURI.endsWith(COLLABORATORS_PART) || httpRequest.requestURI.equals(HOST_PATH)){
            println("skip auth")
            chain?.doFilter(request, response)
            return
        }
        val authorizationHeader: String = httpRequest.getHeader("authorization") ?: ""

        val userInfo = credentialsVerifier(authorizationHeader, collaboratorServices)
        if (userInfo != null) {
            logger.info("User credentials are valid. Proceeding.")
            httpRequest.setAttribute(USER_ATTRIBUTE_KEY, userInfo)
            chain?.doFilter(request, response)
        }
        else {
            logger.info("User credentials are invalid or were not provided. Issuing challenge.")
            val httpResponse = response as HttpServletResponse
            httpResponse.status = HttpServletResponse.SC_UNAUTHORIZED
            //httpResponse.addHeader(CHALLENGE_HEADER, "$BASIC_SCHEME realm=\"pim\"")
        }*/
    }
}