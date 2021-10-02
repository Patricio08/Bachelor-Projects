import React, {useContext} from 'react'
import {
    BrowserRouter as Router,
    useParams
} from "react-router-dom";

import {  useState } from 'react';
import { useFetch2 } from '../services/useFetch2';
import * as Siren from '../Siren'
import { errorDto, nextStateDto, nextStatesDto } from '../models'
import { ListNextStateItem } from './Lists/ListNextStateItem';
import { ListActions } from './Lists/ListActions';

import { PathsContext } from '../App'

type ParamTypes = {
    pid: string,
    stateName: string,
}


export function NextStateList({ }) {
    const [pendingNextState, setPendingNextState] = useState('waiting');

    let { pid, stateName } = useParams<ParamTypes>();

    const pathsContexts = useContext(PathsContext)
    let uri = ''
    if(pathsContexts.fetch){
        if(pendingNextState == "waiting")
        setPendingNextState('')
        let path = pathsContexts.path.nextstate_PATH.replace("{name}", pid)
        path = path.replace("{stateName}", stateName)

        uri = `http://localhost:8081/api${path}`
    }
    
    const fetchState = useFetch2(uri, pendingNextState)


    let toret = <h3>Waiting response</h3>
    let actions = <p></p>

    if (fetchState.type == 'response' && fetchState.status == 200) {
        const body: Siren.Entity<nextStatesDto, nextStateDto> = fetchState.type == 'response' ? JSON.parse(fetchState.body) : ''
        if (body.entities.length == 0) {
            toret = <p>There is no next state</p>
        } else {
            toret = <ListNextStateItem entities={body.entities}/>
            
        }
        if(body.actions != null)
	        actions = <ListActions actions={body.actions} responseField={setPendingNextState} />
    }
    if (fetchState.type == 'response' && fetchState.status != 200) { 
        toret = <p> {fetchState.body} </p>  
    }


    return (
        <div>
			<div>
                {toret}
	            {actions}
			</div>
        </div>
    )
}