package mcauley.daniel.error;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import mcauley.daniel.util.ViewNames;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String defaultErrorHandler(HttpServletRequest req, Exception e, Model model) throws Exception{
        log.error("Exception encountered", e);
        model.addAttribute("exceptionMessage", e.getMessage());
        return ViewNames.ERROR;
    }
}
