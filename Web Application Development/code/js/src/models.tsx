export type projectsDto = {
    collectionSize: number
}

export type projectDto = {
    name: string
    description: string, 
    startState: string
}

export type issuesDto = {
    collectionSize: number
}

export type issueDto = {
    projectName: string,
    id: string,
    name: string,
    description: string,
    beginDate: string,
    stateName: string
}

export type statesDto = {
    collectionSize: number
}

export type stateDto = {
    stateName: string
}

export type commentsDto = {
    collectionSize: number
}

export type commentDto = {
    projectName: string,
    issueId: number,
    id: number,
    text: string,
    date: string
}

export type errorsDto = {
    collectionSize: number
}

export type errorDto = {
    detail: string,
    status: number,
    title: string,
    type: string
}

export type issueLabelsDto = {
    collectionSize: number
}

export type issueLabelDto = {
    labelName: string,
}

export type nextStatesDto = {
    collectionSize: number
}

export type nextStateDto = {
    projectName: string,
    stateName: string,
    nextState: string
}

export type projectStatesDto = {
    collectionSize: number
}

export type projectStateDto = {
    projectName: string,
    stateName: string,
}

export type projectLabelsDto = {
    collectionSize: number   
}

export type projectLabelDto = {
    projectName: string,
    labelName: string,
}
export type ParamTypes = {
    pid: string,
    id: string,
}

