import React from 'react'
import Button from '@material-ui/core/Button';
import { useHistory, useParams, BrowserRouter as Router } from "react-router-dom";
import '../css/Center.css';
import '../css/Shadow.css';
import { CommentsList } from '../components/Comments';

import { ParamTypes } from '../models'

export function CommentsScreen() {
    const history = useHistory();
    const { pid, id } = useParams<ParamTypes>();
    return (
        <div className="screen">
            <div className="screen">
                <div>
                    <div className="screen">
                        <div className="box-shadow">
                            <CommentsList />
                        </div>
                    </div>
                    <div className="screen">
                        <Button variant="contained" onClick={() => history.push(`/project/${pid}/issue/${id}`)}>Go back</Button>
                    </div>
                </div>
            </div>
        </div>
    )
}
