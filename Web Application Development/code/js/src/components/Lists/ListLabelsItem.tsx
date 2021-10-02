import React from 'react'
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';

import * as Siren from '../../Siren'
import { issueLabelsDto, issueLabelDto } from '../../models'

export function ListLabelItem({ entities }: Siren.Entity<issueLabelsDto, issueLabelDto>) {

    return (
        <List component="nav" aria-label="contacts">
            {entities.map((ent) => 
                <ListItem 
                    key={ent.properties.labelName}
                    >
                    <ListItemText inset primary={ent.properties.labelName} />
                </ListItem>)}
        </List>
    )
}
