package br.com.diegosilva.sched.config


import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.NoHandlerFoundException
import javax.servlet.http.HttpServletRequest


class ErrorDTO(var message: String?, var code: Int)

@RestControllerAdvice
class ErrorHandler {

    private val log = LoggerFactory.getLogger(ErrorHandler::class.java)

//    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(DenatranServiceException::class)
//    fun handlerRuntimeException(exception: Exception, request: HttpServletRequest?): ErrorDTO {
//        return ErrorDTO(exception.message, 500)
//    }

//    @ResponseStatus(code = HttpStatus.NOT_FOUND)
//    @ExceptionHandler(ResourceNotFoundException::class)
//    fun handleResource(exception: Exception, request: HttpServletRequest?): ErrorDTO {
//        return ErrorDTO(exception.message, 500)
//    }


    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    fun handleGeneric(exception: Exception, request: HttpServletRequest?): ErrorDTO {
        log.error(exception.message, exception)
        return ErrorDTO(exception.message, 500)
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException::class)
    fun handle404(exception: Exception, request: HttpServletRequest?): ErrorDTO {
        log.error(exception.message, exception)
        return ErrorDTO(exception.message, 500)
    }

    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(exception: Exception?): ErrorDTO {
        return ErrorDTO("Para acessar esta api você precisa estar autenticado.", HttpStatus.FORBIDDEN.value())
    }

}