import React from 'react'
import { useHistory, useParams } from 'react-router-dom';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';

import * as Siren from '../../Siren'
import { issueDto, issuesDto } from '../../models'

import { ParamTypes } from '../../models'

export function ListIssuesItem({ entities }: Siren.Entity<issuesDto, issueDto>) {
    const history = useHistory();
    let { pid } = useParams<ParamTypes>();
    
    return (
        <List component="nav" aria-label="contacts" >
            {entities.map((ent) => 
                <ListItem button 
                    key={ent.properties.id}
                    onClick={() => history.push(`/project/${pid}/issue/${ent.properties.id}`)}
                    >
                    <ListItemText inset primary={ent.properties.name} />
                </ListItem>)}
        </List>
    )
}
