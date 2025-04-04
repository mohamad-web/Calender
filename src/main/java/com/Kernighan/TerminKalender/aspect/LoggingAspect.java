package com.Kernighan.TerminKalender.aspect;

import com.Kernighan.TerminKalender.model.SystemLog;
import com.Kernighan.TerminKalender.repository.SystemLogRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
    private final SystemLogRepository systemLogRepository;

    public LoggingAspect(SystemLogRepository systemLogRepository) {
        this.systemLogRepository = systemLogRepository;
    }

    @Before("execution(* com.Kernighan.TerminKalender.service.*.*(..))")
    public void logMethodCall(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "Unbekannt";

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            username = ((UserDetails) authentication.getPrincipal()).getUsername();
        }


        String actionMessage = getActionMessage(methodName, username, joinPoint.getArgs());


        SystemLog log = new SystemLog();
        log.setAdminUsername(username);
        log.setAction(actionMessage);
        log.setDetails("Methode: " + methodName + " mit Parametern: " + Arrays.toString(joinPoint.getArgs()));

        systemLogRepository.save(log);
    }

    /**
     * verständliche Log-Nachricht.
     */
    private String getActionMessage(String methodName, String username, Object[] args) {
        if (methodName.toLowerCase().contains("create") || methodName.toLowerCase().contains("save")) {
            return "Der Benutzer " + username + " hat einen neuen Termin erstellt.";
        }
        if (methodName.toLowerCase().contains("update") || methodName.toLowerCase().contains("edit")) {
            return "Der Benutzer " + username + " hat einen Termin bearbeitet.";
        }
        if (methodName.toLowerCase().contains("delete") || methodName.toLowerCase().contains("remove")) {
            return "Der Benutzer " + username + " hat einen Termin gelöscht.";
        }
        return "Der Benutzer " + username + " hat die Methode " + methodName + " aufgerufen.";
    }
}