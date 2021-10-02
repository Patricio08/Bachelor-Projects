import React from 'react'
import { useHistory } from 'react-router-dom';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';

import * as Siren from '../../Siren'
import { projectsDto, projectDto } from '../../models'


function getLink(links: Siren.Link[]) {
    let foundLink = "/"
    links.forEach(link => {
        if (link.rel.includes("self")) {
            foundLink = link.href
            return;
        }
            
    })
    return foundLink
}

export function ListProjectsItem({ entities }: Siren.Entity<projectsDto, projectDto>) {
    const history = useHistory();

    return (
        <List component="nav" aria-label="contacts">
            {entities.map((ent) => 
                <ListItem button 
                    key={ent.properties.name}
                    onClick={() => history.push(getLink(ent.links))}
                    >
                    <ListItemText inset primary={ent.properties.name} />
                </ListItem>)}
        </List>
    )
}
