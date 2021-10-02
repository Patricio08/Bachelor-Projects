import React from 'react'
import {
    useHistory,
} from "react-router-dom";

import { ProjectList } from '../components/Projects'
import Button from '@material-ui/core/Button';
import '../css/Center.css';

export function ProjectsScreen() {
    const history = useHistory();
    return (
        <div>
            <div className="screen">
                <div className="box-shadow">
                    <ProjectList />
                </div>
            </div>
            <div className="screen">
                <Button variant="contained" onClick={() => history.push('/')}>Go back</Button>
            </div>
        </div>
    )
}
