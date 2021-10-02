package isel.leic.daw.g29.ProjectIssuesManager.controllers

import org.springframework.web.util.UriTemplate

const val COLLABORATORS_PART = "collaborator"
const val COLLABORATOR_PART = "collaborator/{name}"
const val PROJECTS_PART = "project"
const val LABEL_PART = "label"
const val PROJECT_PART = "project/{name}"
const val ISSUES_PART = "${PROJECT_PART}/issue"
const val ISSUE_PART = "${PROJECT_PART}/issue/{id}"
const val STATE_PART = "state"
const val NEXTSTATE_PART = "${PROJECT_PART}/state/{stateName}/next"
const val PROJECTSTATE_PART = "${PROJECT_PART}/projectstate"
const val PROJECTLABEL_PART = "${PROJECT_PART}/projectlabel"
const val ISSUELABEL_PART = "${ISSUE_PART}/issuelabel"
const val COMMENTS_PART = "${ISSUE_PART}/comment"
const val COMMENT_PART = "${ISSUE_PART}/comment/{commentId}"
const val HOST_PATH = "/"

object Paths {
    val COLLABORATORS_PATH = "${HOST_PATH}${COLLABORATORS_PART}"
    val COLLABORATOR_PATH = "${HOST_PATH}${COLLABORATOR_PART}"
    val PROJECTS_PATH = "${HOST_PATH}${PROJECTS_PART}"
    val PROJECT_PATH = "${HOST_PATH}${PROJECT_PART}"
    val ISSUES_PATH = "${HOST_PATH}${ISSUES_PART}"
    val ISSUE_PATH = "${HOST_PATH}${ISSUE_PART}"
    val STATE_PATH = "${HOST_PATH}${STATE_PART}"
    val PROJECTSTATE_PATH = "${HOST_PATH}${PROJECTSTATE_PART}"
    val PROJECTLABEL_PATH = "${HOST_PATH}${PROJECTLABEL_PART}"
    val NEXTSTATE_PATH = "${HOST_PATH}${NEXTSTATE_PART}"
    val LABEL_PATH = "${HOST_PATH}${LABEL_PART}"
    val ISSUELAVEL_PATH = "${HOST_PATH}${ISSUELABEL_PART}"
    val COMMENTS_PATH = "${HOST_PATH}${COMMENTS_PART}"
    val COMMENT_PATH = "${HOST_PATH}${COMMENT_PART}"

    fun COLLABORATOR_PATH(name: String) = UriTemplate(COLLABORATOR_PATH).expand(name)
    fun PROJECT_PATH(name: String) = UriTemplate(PROJECT_PATH).expand(name)
    fun ISSUES_PATH(name: String) = UriTemplate(ISSUES_PATH).expand(name)
    fun ISSUE_PATH(name: String, id: Int) = UriTemplate(ISSUE_PATH).expand(name, id)
    fun PROJECTSTATE_PATH(name: String) = UriTemplate(PROJECTSTATE_PATH).expand(name)
    fun PROJECTLABEL_PATH(name: String) = UriTemplate(PROJECTLABEL_PATH).expand(name)
    fun NEXTSTATE_PATH(name: String, stateName: String) = UriTemplate(NEXTSTATE_PATH).expand(name, stateName)
    fun ISSUELAVEL_PATH(name: String, id: Int) = UriTemplate(ISSUELAVEL_PATH).expand(name, id)
    fun COMMENTS_PATH(name: String, id: Int) = UriTemplate(COMMENTS_PATH).expand(name, id)
    fun COMMENT_PATH(name: String, id: Int, commentId: Int) = UriTemplate(COMMENT_PATH).expand(name, id, commentId)
}


