import React, {useContext} from 'react'

import {
    BrowserRouter as Router,
    useParams
} from "react-router-dom";

import { useFetch2 } from '../services/useFetch2'
import * as Siren from '../Siren'
import { issuesDto, issueDto } from '../models'
import { ListIssuesItem } from './Lists/ListIssuesItem'
import { ListActions } from './Lists/ListActions';

import { ParamTypes } from '../models'
import { PathsContext } from '../App'

export function IssuesFetch() {
    const [pendingIssue, setPendingIssue] = React.useState('waiting');


    let { pid } = useParams<ParamTypes>();
    const pathsContexts = useContext(PathsContext)

    let uri = ''
    if(pathsContexts.fetch){
        if(pendingIssue == "waiting")
        setPendingIssue('')
        const path = pathsContexts.path.issues_PATH.replace("{name}", pid)
        uri = `http://localhost:8081/api${path}`
    }

    const fetchState = useFetch2(uri, pendingIssue)


    let toret = <h3>Waiting response</h3>
    let actions = <p></p>
    if (fetchState.type == 'response' && fetchState.status == 200) {
        const body: Siren.Entity<issuesDto, issueDto> = fetchState.type == 'response' ? JSON.parse(fetchState.body) : ''

        if (body.entities.length == 0) {
            toret = <p>There is no Issues</p>
        } else {
            toret = <ListIssuesItem entities={body.entities} />
        }
        if (body.actions != null)
            actions = <ListActions actions={body.actions} responseField={setPendingIssue} />
    }

    if (fetchState.type == 'response' && fetchState.status != 200) { 
        toret = <p> {fetchState.body} </p>  
    }

    return (
        <div>
            {toret}
            {actions}
        </div>
    )
}