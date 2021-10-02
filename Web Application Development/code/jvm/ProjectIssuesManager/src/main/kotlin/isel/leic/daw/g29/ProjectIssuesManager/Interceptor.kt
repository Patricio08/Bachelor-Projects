package isel.leic.daw.g29.ProjectIssuesManager

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import java.lang.Exception
import java.text.DecimalFormat
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class Interceptor() : HandlerInterceptor {

    val log: Logger = LoggerFactory.getLogger(Interceptor::class.java)

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any) : Boolean{
        log.info("1. from PreHandle method.")

        val time = System.currentTimeMillis()

        //println(time)
        //request.setAttribute("key", time)


        return true
    }

    override fun postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any, model: ModelAndView?){
        log.info("3. from PostHandle method.")
        val han = handler as HandlerMethod
        val time = request.getAttribute("key")
        /*println(System.currentTimeMillis() - time as Long)
        println(han.beanType.simpleName ) // ProjectController
        println(han.beanType.isAnnotationPresent(RestController::class.java)) // True
        println(han.method.name) // getProjects
        han.method.annotations.map{
            println(it.annotationClass.simpleName) // GetMapping
        }
        println(han.method.isAnnotationPresent(GetMapping::class.java)) // true*/

        request.setAttribute("Handle", han.method.name)

    }

    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, dataObject: Any, e: Exception?) {
        log.info("4. from AfterCompletion method - Request Completed!")
    }
}