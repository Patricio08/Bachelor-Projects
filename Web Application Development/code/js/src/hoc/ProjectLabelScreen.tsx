import React from 'react'
import {
    useHistory,
    useParams
} from "react-router-dom";
import { ProjectLabelList } from '../components/ProjectLabels'
import Button from '@material-ui/core/Button';
import '../css/Center.css';
import { ParamTypes } from '../models'

export function ProjectLabelScreen() {

    const history = useHistory();
    let { pid } = useParams<ParamTypes>();
    return (
        <div>
            <div className="screen">
                <div className="box-shadow">
                    <ProjectLabelList />
                </div>
            </div>
            <div className="screen">
                <Button variant="contained" onClick={() => history.push(`/project/${pid}`)}>Go back</Button>
            </div>
        </div>
    )
}