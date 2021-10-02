import React from 'react'
import Button from '@material-ui/core/Button';
import { useHistory, useParams, BrowserRouter as Router } from "react-router-dom";
import '../css/Center.css';
import { IssuesFetch } from '../components/Issues';

import { ParamTypes } from '../models'

export function IssuesScreen() {
    const history = useHistory();
    let { pid } = useParams<ParamTypes>();
    return (
        <div className="screen">
            <div>
                <div className="screen" >
                    <div className="box-shadow" style={{ minWidth: "400px" }}>
                        <IssuesFetch />
                    </div>
                </div>
                <div className="screen">
                    <Button variant="contained" onClick={() => history.push(`/project/${pid}`)}>Go back</Button>
                </div>
            </div>
        </div>
    )
}
