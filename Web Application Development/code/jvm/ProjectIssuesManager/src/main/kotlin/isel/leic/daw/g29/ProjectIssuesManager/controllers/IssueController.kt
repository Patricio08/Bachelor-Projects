package isel.leic.daw.g29.ProjectIssuesManager.controllers

import isel.leic.daw.g29.ProjectIssuesManager.ProjectIssuesManagerApplication
import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.IssueInputModel
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.IssuesOutputModel
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.SirenEntity
import isel.leic.daw.g29.ProjectIssuesManager.services.ServiceInterface.IIssueService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

private val log = LoggerFactory.getLogger(ProjectIssuesManagerApplication::class.java)

@RestController
class IssueController(private val issueServices: IIssueService) {

    @GetMapping(ISSUES_PART)
    fun getIssues(
            @PathVariable name: String
    ): SirenEntity<IssuesOutputModel> {
        val issues = issueServices.getIssues(name)
                .map { Issue -> Issue.mapToOutputModel() }
        return IssuesOutputModel(name, issues.size).mapToSiren(issues)
    }

    @GetMapping(ISSUE_PART)
    fun getIssue(
            @PathVariable name: String,
            @PathVariable id: Int
    ) = issueServices.getIssue(name, id).mapToOutputModel().mapToSiren()

    @PostMapping(ISSUES_PART)
    fun postIssue(
            @RequestBody input: IssueInputModel,
            @PathVariable name: String,
    ): String {
        input.projectName = name
        return issueServices.postIssue(input)
    }

    @PutMapping(ISSUE_PART)
    fun putIssue(
            @RequestBody input: IssueInputModel,
            @PathVariable name: String,
            @PathVariable id: Int
    ): String {
        println("aquiiiiiiiiii2")
        input.projectName = name
        input.id = id
        return issueServices.putIssue(input)
    }


}