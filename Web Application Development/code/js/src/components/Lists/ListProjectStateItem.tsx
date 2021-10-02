import React from 'react'
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';

import * as Siren from '../../Siren'
import { projectStatesDto, projectStateDto } from '../../models'

export function ListProjectStateItem({ entities }: Siren.Entity<projectStatesDto, projectStateDto>) {

    return (
        <List component="nav" aria-label="contacts">
            {entities.map((ent) => 
                <ListItem 
                    key={ent.properties.stateName}
                    >
                    <ListItemText inset primary={ent.properties.stateName} />
                </ListItem>)}
        </List>
    )
}
