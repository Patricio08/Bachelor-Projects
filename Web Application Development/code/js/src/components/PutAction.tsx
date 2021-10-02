import ListItem from '@material-ui/core/ListItem';
import TextField from '@material-ui/core/TextField';
import React, { useEffect, useState } from 'react'

import * as Siren from '../Siren'
import Button from '@material-ui/core/Button';
import { usePutFetch } from '../services/usePutFetch';


export function PutAction({ action, responseField }: { action: Siren.Action, responseField: React.Dispatch<React.SetStateAction<string>> }) {
    const [responseBody, setresponseBody] = useState(null);
    const [errorState, setErrorState] = React.useState('');
    const PutResponse = usePutFetch('http://localhost:8081/api' + action.href, responseBody)

    useEffect(() => {
        if (PutResponse.type == 'response' && PutResponse.status == 200) {
            responseField(PutResponse.body)
        }
        if (PutResponse.type == 'response' && PutResponse.status != 200){
            setErrorState(JSON.parse(PutResponse.body).detail)
        }
    })

    function handlePutRequest() {
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
                    console.log(field.name)
                    return (
                        <div key={field.name}>
                            <TextField className="standard-basic" id={field.name + " " + action.method} label={field.name} />
                        </div>
                    )
                }
            })
        }
        <Button variant="contained"
            onClick={() => handlePutRequest()}>Update
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