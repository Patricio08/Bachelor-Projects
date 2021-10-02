package isel.leic.daw.g29.ProjectIssuesManager.controllers

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.CollaboratorInputModel
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.CollaboratorsOutputModel
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.SirenEntity
import isel.leic.daw.g29.ProjectIssuesManager.services.ServiceInterface.ICollaboratorService
import org.springframework.web.bind.annotation.*

@RestController
class HomeController(private val CollaboratorServices: ICollaboratorService) {


    @GetMapping("/")
    fun getHome(): Paths = Paths


}