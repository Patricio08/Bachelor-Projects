package isel.leic.daw.g29.ProjectIssuesManager.controllers

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.ProjectInputModel
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.ProjectsOutputModel
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.SirenEntity
import isel.leic.daw.g29.ProjectIssuesManager.services.ServiceInterface.IProjectService
import org.springframework.web.bind.annotation.*


@RestController
class ProjectController(private val ProjectServices: IProjectService) {

    @GetMapping(PROJECTS_PART)
    fun getProjects(): SirenEntity<ProjectsOutputModel> {
        val projects = ProjectServices.getProjects()
                .map { Project -> Project.mapToOutputModel() }
        return ProjectsOutputModel(projects.size).mapToSiren(projects)
    }

    @GetMapping(PROJECT_PART)
    fun getProjectsById(
            @PathVariable name: String
    ) = ProjectServices.getProject(name).mapToOutputModel().mapToSiren()

    @PostMapping(PROJECTS_PART, "application/json")
    fun postProject(
            @RequestBody input: ProjectInputModel,
    ) = ProjectServices.postProject(input)

    @DeleteMapping(PROJECTS_PART)
    fun deleteProject(
        @RequestBody name: ProjectInputModel
    ) = ProjectServices.deleteProject(name)
}