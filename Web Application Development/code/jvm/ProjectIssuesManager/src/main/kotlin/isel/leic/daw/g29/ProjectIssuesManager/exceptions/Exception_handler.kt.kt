package isel.leic.daw.g29.ProjectIssuesManager.exceptions

import isel.leic.daw.g29.ProjectIssuesManager.controllers.ProblemJson
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@RestControllerAdvice
class DbException_handler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(DomainNotFoundException::class)
    @ResponseBody
    fun handleDomainNotFoundException(exception: DomainNotFoundException, request: WebRequest) =
        ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .of(ProblemJson("DomainNotFoundException", exception.name, exception.error, 404))

    @ExceptionHandler(KeyViolationException::class)
    @ResponseBody
    fun handleKeyViolationException(exception: KeyViolationException) =
        ResponseEntity
            .status(HttpStatus.CONFLICT)
            .of(ProblemJson("KeyViolationException", exception.name, exception.error, 409))

    @ExceptionHandler(ForeignKeyException::class)
    @ResponseBody
    fun handleForeignKeyException(exception: ForeignKeyException) =
        ResponseEntity
            .status(HttpStatus.CONFLICT)
            .of(ProblemJson("ForeignKeyException", exception.name, exception.error, 409))

    @ExceptionHandler(NotNullException::class)
    @ResponseBody
    fun handleNotNullException(exception: NotNullException) =
        ResponseEntity
            .status(HttpStatus.CONFLICT)
            .of(ProblemJson("NotNullException", exception.name, exception.error, 409))

    @ExceptionHandler(IntegrityConstraintException::class)
    @ResponseBody
    fun handleIntegrityConstraintException(exception: IntegrityConstraintException) =
        ResponseEntity
            .status(HttpStatus.CONFLICT)
            .of(ProblemJson("IntegrityConstraintException", exception.name, exception.error, 409))

    @ExceptionHandler(CommentArchivedException::class)
    @ResponseBody
    fun handleCommentArchivedException(exception: CommentArchivedException) =
        ResponseEntity
            .status(HttpStatus.CONFLICT)
            .of(ProblemJson("CommentArchivedException", exception.name, exception.error, 409))

    @ExceptionHandler(DbAccessException::class)
    @ResponseBody
    fun handleDbAccessException(exception: DbAccessException) =
        ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .of(ProblemJson("DbAccessException", exception.name, exception.error, 500))


    @ExceptionHandler(AuthenticationException::class)
    @ResponseBody
    fun handleAuthenticationException(exception: AuthenticationException) =
        ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .of(ProblemJson("AuthException", exception.name, exception.error, 401))
}

fun ResponseEntity.BodyBuilder.of(problemJson: ProblemJson) =
    this.contentType(MediaType.APPLICATION_PROBLEM_JSON).body(problemJson)
