import React from 'react'
import {
    useHistory,
    useParams
} from "react-router-dom";
import { IssueLabelList } from '../components/IssueLabel'
import Button from '@material-ui/core/Button';
import '../css/Center.css';

import { ParamTypes } from '../models'

export function IssueLabelScreen() {

    const history = useHistory();
    let { pid, id } = useParams<ParamTypes>();
    return (
        <div>
            <div className="screen">
                <div className="box-shadow">
                    <IssueLabelList />
                </div>
            </div>
            <div className="screen">
                <Button variant="contained" onClick={() => history.push(`/project/${pid}/issue/${id}`)}>Go back</Button>
            </div>
        </div>
    )
}