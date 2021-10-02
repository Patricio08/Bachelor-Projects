import ListItem from '@material-ui/core/ListItem';
import TextField from '@material-ui/core/TextField';
import React, { useEffect } from 'react'

import * as Siren from '../Siren'
import Button from '@material-ui/core/Button';
import { useDeleteFetch } from '../services/useDeleteFetch';
import '../css/Error.css'

export function DeleteAction({ action, responseField }: { action: Siren.Action, responseField: React.Dispatch<React.SetStateAction<string>> }) {
    const [responseBody, setresponseBody] = React.useState(null);
    const [errorState, setErrorState] = React.useState('');

    const deleteResponse = useDeleteFetch('http://localhost:8081/api' + action.href, responseBody)
    useEffect(() => {
        if (deleteResponse.type == 'response' && deleteResponse.status == 200) {
            responseField(deleteResponse.body)
        }
        if (deleteResponse.type == 'response' && deleteResponse.status != 200){
            setErrorState(JSON.parse(deleteResponse.body).detail)
        }
    })

    function handlePostRequest() {
        let body: any = {};
        action.fields.forEach(element => {
            body[element.name] = (document.getElementById(`${element.name} ${action.method}`) as HTMLInputElement).value
        });
        setresponseBody(body)
    }

    let actionInput = <div>
        {
            action.fields.map((field) => {
                if (field.type == "text") {
                    return (
                        <div key={field.name}>
                            <TextField className="standard-basic" id={field.name + " " + action.method} label={field.name} />
                        </div>
                    )
                }
            })
        }
        <Button variant="contained"
            onClick={() => handlePostRequest()}>Delete
        </Button>
        {errorState != '' ? 
            <div className="errorBox">
                {errorState}
            </div>
            :
            <></>
        }
        
    </div>


    return (
        <ListItem >
            {actionInput}
        </ListItem>

    )
}