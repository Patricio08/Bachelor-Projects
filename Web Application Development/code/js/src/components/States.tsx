import React, { useState, useContext } from 'react'

import { useFetch2 } from '../services/useFetch2'
import * as Siren from '../Siren'
import { statesDto, stateDto } from '../models'
import { ListStatesItem } from './Lists/ListStatesItem';
import { ListActions } from './Lists/ListActions';

import { PathsContext } from '../App'


export function StateList() {

    const [pendingState, setPendingState] = useState('waiting');
    const pathsContexts = useContext(PathsContext)

    let uri = ''
    if(pathsContexts.fetch){
        if(pendingState == "waiting")
        setPendingState('')  
        uri = `http://localhost:8081/api${pathsContexts.path.state_PATH}`
    }
    const fetchStates = useFetch2(uri, pendingState)

    

    let toret = <h3>Waiting response</h3>
    let actions = <p></p>
    if (fetchStates.type == 'response' && fetchStates.status === 200) {
        const body: Siren.Entity<statesDto, stateDto> = fetchStates.type == 'response' ? JSON.parse(fetchStates.body) : ''
        if (body.entities.length == 0) {
            toret = <p>There is no states</p>
        } else {
            toret = <ListStatesItem entities={body.entities} />

        }

        if (body.actions != null)
            actions = <ListActions actions={body.actions} responseField={setPendingState} />
    }

    if (fetchStates.type == 'response' && fetchStates.status != 200) { 
        toret = <p> {fetchStates.body} </p>  
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
