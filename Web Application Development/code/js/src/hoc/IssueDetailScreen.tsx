import React from 'react'
import Button from '@material-ui/core/Button';
import { useHistory, useParams, BrowserRouter as Router } from "react-router-dom";
import '../css/Center.css';
import { IssueByIdFetch } from '../components/IssueDetails';

import { ParamTypes } from '../models'

export function IssueDetailScreen() {
    const history = useHistory();
    const { pid } = useParams<ParamTypes>();
    return (
        <div className="screen">
            <div className="screen">
                <div>
                    <div className="screen">
                        <div className="box-shadow">
                            <IssueByIdFetch />
                        </div>
                    </div>
                    <div className="screen">
                        <Button variant="contained" onClick={() => history.push(`/project/${pid}/issue`)}>Go back</Button>
                    </div>
                </div>
            </div>
        </div>
    )
}
