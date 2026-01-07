import styled,{css} from 'styled-components';

export type Align = 'left' | 'center' | 'right';
export type HeaderVariant = 'default' | 'dark' | 'border';

export const BaseTable = styled.table`
width:100%; border-collapse:collapse;  table-layout:fixed;
`;

const headerVariants: Record<HeaderVariant, ReturnType<typeof css>> = {
 default:css`
 background:#f5f5f5; color:#333;
 `,
 dark:css`
 background:#333; color:#fff; 
 `,
 border:css`
 background:#fff; border-bottom:2px solid #000;
 `,
};