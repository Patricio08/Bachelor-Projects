package isel.leic.daw.g29.ProjectIssuesManager.controllers


import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.CollaboratorInputModel
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.CollaboratorsOutputModel
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.SirenEntity
import isel.leic.daw.g29.ProjectIssuesManager.services.ServiceInterface.ICollaboratorService
import org.springframework.web.bind.annotation.*


@RestController
class CollaboratorController(private val CollaboratorServices: ICollaboratorService) {

    @PostMapping(COLLABORATORS_PART, "application/json")
    fun postCollaborator(
            @RequestBody input: CollaboratorInputModel
    ) = CollaboratorServices.postCollaborator(input)

    @GetMapping(COLLABORATORS_PART)
    fun getCollaborators(): SirenEntity<CollaboratorsOutputModel> {
        val collaborators = CollaboratorServices.getCollaborators()
                .map { Collaborator -> Collaborator.mapToOutputModel() }
        return CollaboratorsOutputModel(collaborators.size).mapToSiren(collaborators)
    }

    @GetMapping(COLLABORATOR_PART)
    fun getCollaboratorsById(
            @PathVariable name: String
    ) = CollaboratorServices.getCollaborator(name).mapToOutputModel().mapToSiren()

}


