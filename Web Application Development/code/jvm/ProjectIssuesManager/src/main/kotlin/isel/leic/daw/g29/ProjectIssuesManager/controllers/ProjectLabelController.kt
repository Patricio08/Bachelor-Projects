package isel.leic.daw.g29.ProjectIssuesManager.controllers

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.ProjectLabelInputModel
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.ProjectLabelsOutputModel
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.SirenEntity
import isel.leic.daw.g29.ProjectIssuesManager.services.ServiceInterface.IProjectLabelService
import org.springframework.web.bind.annotation.*

@RestController
class ProjectLabelController(private val projectLabelServices: IProjectLabelService) {

    @GetMapping("project/{name}/projectlabel")
    fun getProjects(
            @PathVariable name: String
    ): SirenEntity<ProjectLabelsOutputModel> {
        val projects = projectLabelServices.getProjectLabels(name)
                .map { Project -> Project.mapToOutputModel() }
        return ProjectLabelsOutputModel(name, projects.size).mapToSiren(projects)
    }

    @PostMapping("project/{name}/projectlabel", "application/json")
    fun postLabel(
            @RequestBody projectLabel: ProjectLabelInputModel,
            @PathVariable name: String
    ): String {
        projectLabel.projectName = name
        return projectLabelServices.postProjectLabel(projectLabel)
    }

    @DeleteMapping("project/{name}/projectlabel", "application/json")
    fun deleteLabel(
            @RequestBody projectLabel: ProjectLabelInputModel,
            @PathVariable name: String
    ): String {
        projectLabel.projectName = name
        return projectLabelServices.deleteProjectLabel(projectLabel)
    }
}