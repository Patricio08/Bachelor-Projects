package isel.leic.daw.g29.ProjectIssuesManager.controllers

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.ProjectStateInputModel
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.ProjectStatesOutputModel
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.SirenEntity
import isel.leic.daw.g29.ProjectIssuesManager.services.ServiceInterface.IProjectStateService
import org.springframework.web.bind.annotation.*


@RestController
class ProjectStateController(private val projectStateServices: IProjectStateService) {

    @GetMapping(PROJECTSTATE_PART)
    fun getProjectStates(
            @PathVariable name: String
    ): SirenEntity<ProjectStatesOutputModel> {
        val project_states = projectStateServices.getProjectStates(name)
                .map { ProjectState -> ProjectState.mapToOutputModel() }
        return ProjectStatesOutputModel(name, project_states.size).mapToSiren(project_states)
    }

    @PostMapping(PROJECTSTATE_PART, "application/json")
    fun postProjectState(
            @PathVariable name: String,
            @RequestBody input: ProjectStateInputModel
    ): String {
        input.projectName = name
        return projectStateServices.postProjectState(input)
    }

    @DeleteMapping(PROJECTSTATE_PART, "application/json")
    fun deleteProjectState(
        @PathVariable name: String,
        @RequestBody input: ProjectStateInputModel
    ): String {
        input.projectName = name
        return projectStateServices.deleteProjectState(input)
    }

}