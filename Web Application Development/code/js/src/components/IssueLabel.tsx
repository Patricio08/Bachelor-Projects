import React, {useContext} from 'react'
import {
    BrowserRouter as Router,
    useParams
} from "react-router-dom";

import { useState } from 'react';

import { useFetch2 } from '../services/useFetch2';
import * as Siren from '../Siren'
import { issueLabelsDto, issueLabelDto } from '../models'
import { ListIssueLabelItem } from './Lists/ListIssueLabelItem';
import { ListActions } from './Lists/ListActions';

import { PathsContext } from '../App'

type ParamTypes = {
    pid: string,
    id: string,
}


export function IssueLabelList({ }) {
    const [pendingIssueLabel, setPendingIssueLabel] = useState('waiting');


    let { pid, id } = useParams<ParamTypes>();
    const pathsContexts = useContext(PathsContext)

    let uri = ''
    if(pathsContexts.fetch){
        if(pendingIssueLabel == "waiting")
        setPendingIssueLabel('')
        let path = pathsContexts.path.issuelavel_PATH.replace("{name}", pid)
        path = path.replace("{id}", id)

        uri = `http://localhost:8081/api${path}`
    }

    
    const fetchIssueLabel = useFetch2(uri, pendingIssueLabel)

    const body: Siren.Entity<issueLabelsDto, issueLabelDto> = fetchIssueLabel.type == 'response' ? JSON.parse(fetchIssueLabel.body) : ''

    let toret = <h3>Waiting response</h3>
    let actions = <p></p>

    if (fetchIssueLabel.type == 'response' && fetchIssueLabel.status === 200) { 
        if (body.entities.length == 0) {
            toret = <p>There is no issue labels</p>
        } else {
            toret = <ListIssueLabelItem entities={body.entities} />
        }
        if(body.actions != null)
	        actions = <ListActions actions={body.actions} responseField={setPendingIssueLabel} />
    }

    if (fetchIssueLabel.type == 'response' && fetchIssueLabel.status != 200) { 
        toret = <p> {fetchIssueLabel.body} </p>  
    }

    return ( 

        <div>
            {toret}
            {actions}
        </div>
   
    )
}