import React, { useContext } from 'react'
import {
    BrowserRouter as Router,
    useHistory,
    useParams
} from "react-router-dom";
import Button from '@material-ui/core/Button';

import { useFetch2 } from '../services/useFetch2'
import * as Siren from '../Siren'
import { issueDto } from '../models'
import { ListActions } from './Lists/ListActions';
import { PathsContext } from '../App'

import { ParamTypes } from '../models'

export function IssueByIdFetch({ }) {
    const [pendingIssue, setPendingIssue] = React.useState('waiting');
    const history = useHistory();
    let { pid, id } = useParams<ParamTypes>();

    const pathsContexts = useContext(PathsContext)

    let uri = ''
    if(pathsContexts.fetch){
        if(pendingIssue == "waiting")
        setPendingIssue('')
        const path1 = pathsContexts.path.issue_PATH.replace("{name}", pid)
        const path = path1.replace("{id}", id)
    
        uri = `http://localhost:8081/api${path}`
    }

    const fetchState = useFetch2(uri, pendingIssue)
    console.log(uri)

    const status = fetchState.type == 'response' ? fetchState.status : '?'
    const body: Siren.SubEntity<issueDto> = fetchState.type == 'response' ? JSON.parse(fetchState.body) : ''
    const state = fetchState.type

    let toret = <h3>Waiting response</h3>
    let actions = <p></p>

    if (fetchState.type == 'response') {
        if (fetchState.status == 200) {

            const date = new Date(body.properties.beginDate);
            toret = <div>
                <p>{body.properties.id}</p>
                <p>{body.properties.name}</p>
                <p>{body.properties.projectName}</p>
                <p>{body.properties.stateName}</p>
                <p>{date.toString()}</p>

                <Button variant="contained"
                    onClick={() => history.push(`/project/${pid}/issue/${id}/comment`)}>Go to comments</Button>
                <Button variant="contained"
                    onClick={() => history.push(`/project/${pid}/state/${body.properties.stateName}/next`)}>Check Next States</Button>
                <Button variant="contained"
                    onClick={() => history.push(`/project/${pid}/issue/${id}/issuelabel`)}>Check Issue Labels</Button>
            </div>
            if (body.actions != null)
                actions = <ListActions actions={body.actions} responseField={setPendingIssue} />
        } else {
            <p> Invalid issue </p>
        }
    }

    return (
        <div>
            <p>URI: {uri}</p>

            <p>{status}</p>

            <p>{state}</p>

            {toret}
            {actions}

        </div>
    )
}