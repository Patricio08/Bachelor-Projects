import React, {useContext} from 'react'
import {
    BrowserRouter as Router,
    useParams
} from "react-router-dom";

import { useFetch2 } from '../services/useFetch2'
import * as Siren from '../Siren'
import { commentsDto, commentDto } from '../models'
import { ListCommentsItem } from './Lists/ListCommentsItem'
import { ListActions } from './Lists/ListActions'

import { ParamTypes } from '../models'
import '../css/Error.css'

import { PathsContext } from '../App'

export function CommentsList({ }) {
    const [pendingComment, setPendingComment] = React.useState('waiting');

    let { pid, id } = useParams<ParamTypes>();

    const pathsContexts = useContext(PathsContext) 
    console.log("aquiui" + pathsContexts)
    let uri = ''
    if(pathsContexts.fetch){
        if(pendingComment == "waiting")
        setPendingComment('')
        let path = pathsContexts.path.comments_PATH.replace("{name}", pid)
        path = path.replace("{id}", id)

        uri = `http://localhost:8081/api${path}`
    }

    

    const fetchState = useFetch2(uri, pendingComment)

    const status = fetchState.type == 'response' ? fetchState.status : '?'

    const state = fetchState.type

    let toret = <h3>Waiting response</h3>
    let actions = <p></p>
    if (fetchState.type == 'response' && fetchState.status == 200) {
        const body: Siren.Entity<commentsDto, commentDto> = fetchState.type == 'response' ? JSON.parse(fetchState.body) : ''

        if (body.entities.length == 0) {
            toret = <p>There is no comments</p>
        } else {
            toret = <ListCommentsItem entities={body.entities} />
        }
        if (body.actions != null)
            actions = <ListActions actions={body.actions} responseField={setPendingComment} />

    }

    if (fetchState.type == 'response' && fetchState.status != 200) { 
        toret = <div className="errorBox">
                <p> {fetchState.body} </p>
            </div>  
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