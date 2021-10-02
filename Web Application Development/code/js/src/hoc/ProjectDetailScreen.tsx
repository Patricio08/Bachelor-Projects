import React from 'react'

import Button from '@material-ui/core/Button';
import { useHistory, BrowserRouter as Router } from "react-router-dom";
import { ProjectByIdFetch } from '../components/ProjectDetails'

import '../css/Center.css';

export function ProjectDetailScreen() {
    const history = useHistory();
    return (
        <div>
            <div className="screen">
                <div className="box-shadow">
                    <ProjectByIdFetch />
                </div>
            </div>
            <div className="screen">
                <Button variant="contained" onClick={() => history.push('/project')}>Go back</Button>
            </div>  
        </div>
        
    )
}
