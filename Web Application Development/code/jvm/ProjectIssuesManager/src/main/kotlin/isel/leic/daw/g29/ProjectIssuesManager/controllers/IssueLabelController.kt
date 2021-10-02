package isel.leic.daw.g29.ProjectIssuesManager.controllers

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.IssueLabelInputModel
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.IssueLabelsOutputModel
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.SirenEntity
import isel.leic.daw.g29.ProjectIssuesManager.services.ServiceInterface.IIssueLabelService
import org.springframework.web.bind.annotation.*

@RestController
class IssueLabelController(private val issueLabelService: IIssueLabelService) {
    @GetMapping(ISSUELABEL_PART)
    fun getLabels(
            @PathVariable name: String,
            @PathVariable id: Int
    ): SirenEntity<IssueLabelsOutputModel> {
        val issuelabels = issueLabelService.getIssueLabel(name, id)
                .map { IssueLabel -> IssueLabel.mapToOutputModel() }
        return IssueLabelsOutputModel(name, id, issuelabels.size).mapToSiren(issuelabels)
    }

    @PostMapping(ISSUELABEL_PART, "application/json")
    fun postLabel(
            @RequestBody issueLabel: IssueLabelInputModel,
            @PathVariable name: String,
            @PathVariable id: Int
    ): String {
        issueLabel.id = id
        issueLabel.projectName = name
        return issueLabelService.postIssueLabel(issueLabel)
    }

    @DeleteMapping(ISSUELABEL_PART, "application/json")
    fun deleteLabel(
            @RequestBody issueLabel: IssueLabelInputModel,
            @PathVariable name: String,
            @PathVariable id: Int
    ): String {
        issueLabel.id = id
        issueLabel.projectName = name
        return issueLabelService.deleteIssueLabel(issueLabel)
    }
}