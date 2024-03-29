package edu.kalum.notas.core.controllers;

import edu.kalum.notas.core.models.entities.Alumno;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// http://localhost:9002/kalum-notas/v1/alumnos

import edu.kalum.notas.core.models.entities.AsignacionAlumno;
import edu.kalum.notas.core.models.services.IAlumnoServices;
import edu.kalum.notas.core.models.services.IAsignacionAlumnoServices;

import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping({"/kalum-notas/v1"})
public class AlumnoController {
    private Logger logger = LoggerFactory.getLogger(edu.kalum.notas.core.controllers.AlumnoController.class);

    @Autowired
    private IAlumnoServices alumnoService;

    @Autowired
    private IAsignacionAlumnoServices asignacionAlumnoServices;

    @GetMapping({"/alumnos"})
    public ResponseEntity<?> listarAlumnos() {
        Map<String, Object> response = new HashMap<>();
        this.logger.debug("Iniaciando el proceso de la consulta de alumnos en la base de datos.");
        try {
            this.logger.debug("Iniciando la consulta de base de datos.");
            List<Alumno> listaAlumnos = this.alumnoService.findAll();
            if (listaAlumnos == null || listaAlumnos.size() == 0) {
                this.logger.warn("No existen registros en la tabla de alumnos.");
                response.put("Mensaje", "No existen registros en la tabla alumos.");
                return new ResponseEntity(response, HttpStatus.NO_CONTENT);
            }
            this.logger.info("Obteniendo listado de la informacion de alumnos.");
            return new ResponseEntity(listaAlumnos, HttpStatus.OK);
        } catch (CannotCreateTransactionException e) {
            this.logger.error("Error al momento de conectarse a la base de datos.");
            response.put("Mensaje", "No error al momento de conectarse a la base de datos.");
            response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity(response, HttpStatus.SERVICE_UNAVAILABLE);
        } catch (DataAccessException e) {
            this.logger.error("Error al momento de conectarse a la base de Datos.");
            response.put("Mensaje", "Error al momento de consultar la informacion a la base de datos.");
            response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity(response, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @GetMapping({"/alumnos/{carne}/asignaciones"})
    public ResponseEntity<?> listarAsignaciones(@PathVariable String carne) {
        Map<String, Object> response = new HashMap<>();
        this.logger.debug("Iniaciando el proceso de la consulta de asignaciones por alumno.");
        try {
            this.logger.debug("Iniciando la consulta de base de datos.");
            List<AsignacionAlumno> listaDeAsignaciones = this.asignacionAlumnoServices.findAllByCarne(carne);
            if (listaDeAsignaciones == null || listaDeAsignaciones.size() == 0) {
                this.logger.warn("No existen registros en la tabla de asignaciones.");
                response.put("Mensaje", "No existen registros en la tabla asignaciones.");
                return new ResponseEntity(response, HttpStatus.NO_CONTENT);
            }
            this.logger.info("Obteniendo listado de la informacion de alumnos.");
            return new ResponseEntity(listaDeAsignaciones, HttpStatus.OK);
        } catch (CannotCreateTransactionException e) {
            this.logger.error("Error al momento de conectarse a la base de datos.");
            response.put("Mensaje", "No error al momento de conectarse a la base de datos.");
            response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity(response, HttpStatus.SERVICE_UNAVAILABLE);
        } catch (DataAccessException e) {
            this.logger.error("Error al momento de conectarse a la base de Datos.");
            response.put("Mensaje", "Error al momento de consultar la informacion a la base de datos.");
            response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity(response, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @GetMapping({"/alumnos/{carne}"})
    public ResponseEntity<?> showAlumnos(@PathVariable String carne) {
        Map<String, Object> response = new HashMap<>();
        this.logger.debug("Iniciando el proceso de la consulta de alumnos en la base de datos.");
        try {
            this.logger.debug("Iniciando la consulta de base de datos por numero de carne: ".concat(carne));
            Alumno alumno = this.alumnoService.findByCarne(carne);
            if (alumno == null) {
                this.logger.warn("No existen registros en la tabla de alumnos con el carne: ".concat(carne));
                response.put("Mensaje", "No existen registros en la tabla de alumnos con el carne: ".concat(carne));
                return new ResponseEntity(response, HttpStatus.NOT_FOUND);
            }
            this.logger.info("Obteniendo informacia la base de datos.".concat(carne));
            return new ResponseEntity(alumno, HttpStatus.OK);
        } catch (CannotCreateTransactionException e) {
            this.logger.error("Error al momento de conectarse a la base de datos.");
            response.put("Mensaje", "No error al momento de conectarse a la base de datos.");
            response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity(response, HttpStatus.SERVICE_UNAVAILABLE);
        } catch (DataAccessException e) {
            this.logger.error("Error al momento de conectarse a la base de datos.");
            response.put("Mensaje", "Error al momento de consultar la informacion a la base de datos.");
            response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity(response, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @GetMapping({"/alumnos/filter"})
    public ResponseEntity<?> showAlumnoByEmail(@RequestParam String email) {
        Map<String, Object> response = new HashMap<>();
        this.logger.debug("Iniciando el proceso de la consulta de alumnos en la base de datos.");
        try {
            this.logger.debug("Iniciando la consulta de base de datos por el email: ".concat(email));
            Alumno alumno = this.alumnoService.findByEmail(email);
            if (alumno == null) {
                this.logger.warn("No existe en la tabla de alumnos con el email: ".concat(email));
                response.put("Mensaje", "Error al conectarse a la base de datos.");
                return new ResponseEntity(response, HttpStatus.NOT_FOUND);
            }
            this.logger.info("Obteniendo informacia la base de datos.".concat(email));
            return new ResponseEntity(alumno, HttpStatus.OK);
        } catch (CannotCreateTransactionException e) {
            this.logger.error("Error al momento de conectarse a la base de datos.");
            response.put("Mensaje", "No error al momento de conectarse a la base de datos.");
            response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity(response, HttpStatus.SERVICE_UNAVAILABLE);
        } catch (DataAccessException e) {
            this.logger.error("Error al momento de conectarse a la base de Datos.");
            response.put("Mensaje", "Error al momento de consultar la informacion a la base de datos.");
            response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity(response, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}